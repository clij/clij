__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


inline void copyNeighborhoodToArray(DTYPE_IMAGE_IN_2D src, DTYPE_OUT array[],
                                    const int2 coord,
                                    const int Nx, const int Ny ) {
    // centers
    const int4   e = (int4)  { (Nx-1)/2, (Ny-1)/2, 0, 0 };

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                array[count] = (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x;
                count++;
            }
        }
    }
}


inline void copySliceNeighborhoodToArray(DTYPE_IMAGE_IN_3D src, DTYPE_OUT array[],
                                    const int4 coord,
                                    const int Nx, const int Ny ) {
    // centers
    const int4   e = (int4)  { (Nx-1)/2, (Ny-1)/2, 0, 0 };

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                array[count] = (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord+((int4){x,y,0,0})).x;
                count++;
            }
        }
    }
}


inline int copyVolumeNeighborhoodToArray(DTYPE_IMAGE_IN_3D src, DTYPE_OUT array[],
                                    const int4 coord,
                                    const int Nx, const int Ny, const int Nz ) {
    // centers
    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 };

    int count = 0;

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;
    float cSquared = e.z * e.z;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            for (int z = -e.z; z <= e.z; z++) {
                float zSquared = z * z;
                if (xSquared / aSquared + ySquared / bSquared + zSquared / cSquared <= 1.0) {

                    int x1 = coord.x + x;
                    int x2 = coord.y + y;
                    int x3 = coord.z + z;
                    const int4 pos = (int4){x1,x2,x3,0};
                    float value_res = (float)READ_IMAGE_3D(src,sampler,pos).x;
                    array[count] = value_res;
                    count++;
                }
            }
        }
    }
    return count;
}

inline void sort(DTYPE_OUT array[], int array_size)
{
    DTYPE_OUT temp;
    for(int i = 0; i < array_size; i++) {
        int j;
        temp = array[i];
        for(j = i - 1; j >= 0 && temp < array[j]; j--) {
            array[j+1] = array[j];
        }
        array[j+1] = temp;
    }
}

inline DTYPE_OUT average(DTYPE_OUT array[], int array_size)
{
    DTYPE_OUT sum = 0;
    for(int i = 0; i < array_size; i++) {
        sum += array[i];
    }
    return sum / array_size;
}

inline DTYPE_OUT median(DTYPE_OUT array[], int array_size)
{
    sort(array, array_size);
    return array[array_size / 2];
}

__kernel void mean_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    DTYPE_OUT sum = 0;
    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                sum += (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord+((int4){x,y,0,0})).x;
                count++;
            }
        }
    }

  DTYPE_OUT res = sum / count;
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void mean_image2d_ij
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int radius
)
{
    const int i = get_global_id(0), j = get_global_id(1);
    const int2 coord = (int2){i,j};

    // centers
    const int4   e = (int4)  { radius, radius, 0, 0 };

    float rSquared = pow((float)radius, 2) + 1;

    int count = 0;

    float sum = 0;
    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared + ySquared <= rSquared) {
                sum += (float)(READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x);
                count++;
            }
        }
    }


    DTYPE_OUT res = (sum / count + 0.5);
    WRITE_IMAGE_2D(dst, coord, res);
}


__kernel void mean_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };
    int count = 0;
    float sum = 0;

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

  for (int x = -e.x; x <= e.x; x++) {
      float xSquared = x * x;
      for (int y = -e.y; y <= e.y; y++) {
          float ySquared = y * y;
          if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
              sum += (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x;
              count++;
          }
      }
  }

  DTYPE_OUT res = sum / count;
  WRITE_IMAGE_2D(dst, coord, res);
}

__kernel void mean_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 };
    int count = 0;
    float sum = 0;

    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;
    float cSquared = e.z * e.z;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            for (int z = -e.z; z <= e.z; z++) {
                float zSquared = z * z;
                if (xSquared / aSquared + ySquared / bSquared + zSquared / cSquared <= 1.0) {

                    int x1 = coord.x + x;
                    int x2 = coord.y + y;
                    int x3 = coord.z + z;
                    const int4 pos = (int4){x1,x2,x3,0};
                    float value_res = (float)READ_IMAGE_3D(src,sampler,pos).x;
                    sum += value_res;
                    count++;
                }
            }
        }
    }


  DTYPE_OUT res = sum / count;
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void median_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copyNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE_2D(dst, coord, res);
}


__kernel void median_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

  int array_size = Nx * Ny * Nz;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  array_size = copyVolumeNeighborhoodToArray(src, array, coord, Nx, Ny, Nz);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void median_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void minimum_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };

    DTYPE_OUT minimumValue = (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord+((int4){x,y,0,0})).x;
                if (value < minimumValue) {
                    minimumValue = value;
                }
            }
        }
    }

  DTYPE_OUT res = minimumValue;
  WRITE_IMAGE_3D(dst, coord, res);
}


__kernel void minimum_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};

    const int4   e = (int4)  { (Nx-1)/2, (Ny-1)/2, 0, 0 };

    DTYPE_OUT minimumValue = (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x;
                if (value < minimumValue) {
                    minimumValue = value;
                }
            }
        }
    }

  DTYPE_OUT res = minimumValue;
  WRITE_IMAGE_2D(dst, coord, res);
}

__kernel void minimum_image2d_ij
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int radius
)
{
    const int i = get_global_id(0), j = get_global_id(1);
    const int2 coord = (int2){i,j};

    // centers
    const int4   e = (int4)  { radius, radius, 0, 0 };

    float rSquared = pow((float)radius, 2) + 1;

    float minimumValue = (float)(READ_IMAGE_2D(src,sampler,coord).x);
    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared + ySquared <= rSquared) {
                float value = (float)(READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x);
                if (value < minimumValue) {
                    minimumValue = value;
                }
            }
        }
    }

    DTYPE_OUT res = minimumValue;
    WRITE_IMAGE_2D(dst, coord, res);
}


__kernel void minimum_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 };
    float minimumValue = (float)READ_IMAGE_3D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;
    float cSquared = e.z * e.z;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            for (int z = -e.z; z <= e.z; z++) {
                float zSquared = z * z;
                if (xSquared / aSquared + ySquared / bSquared + zSquared / cSquared <= 1.0) {

                    int x1 = coord.x + x;
                    int x2 = coord.y + y;
                    int x3 = coord.z + z;
                    const int4 pos = (int4){x1,x2,x3,0};
                    float value_res = (float)READ_IMAGE_3D(src,sampler,pos).x;
                    if (value_res < minimumValue) {
                        minimumValue = value_res;
                    }
                }
            }
        }
    }


  DTYPE_OUT res = minimumValue;
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void maximum_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};

    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, 0, 0 };
    DTYPE_OUT maximumValue = (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_3D(src,sampler,coord+((int4){x,y,0,0})).x;
                if (value > maximumValue) {
                    maximumValue = value;
                }
            }
        }
    }

  DTYPE_OUT res = maximumValue;
  WRITE_IMAGE_3D(dst, coord, res);
}


__kernel void maximum_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2){i,j};
    const int4   e = (int4)  { (Nx-1)/2, (Ny-1)/2, 0, 0 };

    DTYPE_OUT maximumValue = (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;

    int count = 0;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared / aSquared + ySquared / bSquared <= 1.0) {
                DTYPE_OUT value = (DTYPE_OUT)READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x;
                if (value > maximumValue) {
                    maximumValue = value;
                }
            }
        }
    }

  DTYPE_OUT res = maximumValue;

  WRITE_IMAGE_2D(dst, coord, res);
}

__kernel void maximum_image2d_ij
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int radius
)
{
    const int i = get_global_id(0), j = get_global_id(1);
    const int2 coord = (int2){i,j};

    // centers
    const int4   e = (int4)  { radius, radius, 0, 0 };

    float rSquared = pow((float)radius, 2) + 1;

    float maximumValue = (float)(READ_IMAGE_2D(src,sampler,coord).x);
    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            if (xSquared + ySquared <= rSquared) {
                float value = (float)(READ_IMAGE_2D(src,sampler,coord+((int2){x,y})).x);
                if (value > maximumValue) {
                    maximumValue = value;
                }
            }
        }
    }

    DTYPE_OUT res = maximumValue;
    WRITE_IMAGE_2D(dst, coord, res);
}


__kernel void maximum_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4){i,j,k,0};


    const int4   e = (int4)  {(Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 };
    float maximumValue = (float)READ_IMAGE_3D(src,sampler,coord).x;
    float aSquared = e.x * e.x;
    float bSquared = e.y * e.y;
    float cSquared = e.z * e.z;

    for (int x = -e.x; x <= e.x; x++) {
        float xSquared = x * x;
        for (int y = -e.y; y <= e.y; y++) {
            float ySquared = y * y;
            for (int z = -e.z; z <= e.z; z++) {
                float zSquared = z * z;
                if (xSquared / aSquared + ySquared / bSquared + zSquared / cSquared <= 1.0) {

                    int x1 = coord.x + x;
                    int x2 = coord.y + y;
                    int x3 = coord.z + z;
                    const int4 pos = (int4){x1,x2,x3,0};
                    float value_res = (float)READ_IMAGE_3D(src,sampler,pos).x;
                    if (value_res > maximumValue) {
                        maximumValue = value_res;
                    }
                }
            }
        }
    }

  DTYPE_OUT res = maximumValue;
  WRITE_IMAGE_3D(dst, coord, res);
}

__kernel void mean_sep_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  const int4 dir   = (int4)(dim==0,dim==1,dim==2,0);

  // center
  const int   c = (N-1)/2;

  float res = 0, count = 0;
  for (int v = -c; v <= c; v++) {
    res += (float)READ_IMAGE_3D(src,sampler,coord+v*dir).x;
    count += 1;
  }
  res /= count;
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}


__kernel void min_sep_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  const int4 dir   = (int4)(dim==0,dim==1,dim==2,0);

  // center
  const int   c = (N-1)/2;

  float res = READ_IMAGE_3D(src,sampler,coord).x;
  for (int v = -c; v <= c; v++) {
    res = min(res, (float)READ_IMAGE_3D(src,sampler,coord+v*dir).x);
  }
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}

__kernel void max_sep_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  const int4 dir   = (int4)(dim==0,dim==1,dim==2,0);

  // center
  const int   c = (N-1)/2;

  float res = READ_IMAGE_3D(src,sampler,coord).x;
  for (int v = -c; v <= c; v++) {
    res = max(res, (float)READ_IMAGE_3D(src,sampler,coord+v*dir).x);
  }
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}


__kernel void mean_sep_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);
  const int2 dir   = (int2)(dim==0,dim==1);

  // center
  const int   c = (N-1)/2;

  float res = 0, count = 0;
  for (int v = -c; v <= c; v++) {
    res += (float)READ_IMAGE_2D(src,sampler,coord+v*dir).x;
    count += 1;
  }
  res /= count;
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}

__kernel void min_sep_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);
  const int2 dir   = (int2)(dim==0,dim==1);

  // center
  const int   c = (N-1)/2;

  float res = (float)(READ_IMAGE_2D(src,sampler,coord).x);
  for (int v = -c; v <= c; v++) {
    if (v != 0) {
      res = min(res, (float)(READ_IMAGE_2D(src,sampler,coord+v*dir).x));
    }
  }
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}


__kernel void max_sep_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int dim, const int N, const float s
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);
  const int2 dir   = (int2)(dim==0,dim==1);

  // center
  const int   c = (N-1)/2;

  float res = (float)READ_IMAGE_2D(src,sampler,coord).x;
  for (int v = -c; v <= c; v++) {
    if (v != 0) {
      res = max(res, (float)(READ_IMAGE_2D(src,sampler,coord+v*dir).x));
    }
  }
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}

