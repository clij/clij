
__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


inline void copyNeighborhoodToArray(DTYPE_IMAGE_IN_2D src, DTYPE_OUT array[],
                                    const int2 coord,
                                    const int Nx, const int Ny ) {
    // centers
    const int4   c = (int4)  ( (Nx-1)/2, (Ny-1)/2, 0, 0 );

    int count = 0;

    for (int x = -c.x; x <= c.x; x++) {
        for (int y = -c.y; y <= c.y; y++) {
            array[count] = (DTYPE_OUT)READ_IMAGE(src,sampler,coord+(int2)(x,y)).x;
            count++;
        }
    }
}


inline void copySliceNeighborhoodToArray(DTYPE_IMAGE_IN_3D src, DTYPE_OUT array[],
                                    const int4 coord,
                                    const int Nx, const int Ny ) {
    // centers
    const int4   c = (int4)  ( (Nx-1)/2, (Ny-1)/2, 0, 0 );

    int count = 0;

    for (int x = -c.x; x <= c.x; x++) {
        for (int y = -c.y; y <= c.y; y++) {
            array[count] = (DTYPE_OUT)READ_IMAGE(src,sampler,coord+(int4)(x,y,0,0)).x;
            count++;
        }
    }
}


inline int copyVolumeNeighborhoodToArray(DTYPE_IMAGE_IN_3D src, DTYPE_OUT array[],
                                    const int4 coord,
                                    const int Nx, const int Ny, const int Nz ) {
    // centers
    const int4   c = (int4)  ( (Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 );

    int count = 0;

    float aSquared = c.x * c.x;
    float bSquared = c.y * c.y;
    float cSquared = c.z * c.z;

    for (int x = -c.x; x <= c.x; x++) {
        float xSquared = x * x;
        for (int y = -c.y; y <= c.y; y++) {
            float ySquared = y * y;
            for (int z = -c.z; z <= c.z; z++) {
                float zSquared = z * z;
                if (xSquared / aSquared + ySquared / bSquared + zSquared / cSquared <= 1.0) {
                    array[count] = (DTYPE_OUT)READ_IMAGE(src,sampler,coord+(int4)(x,y,z,0)).x;
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


inline DTYPE_OUT minimum(DTYPE_OUT array[], int array_size)
{
    DTYPE_OUT minimum = array[0];
    for(int i = 1; i < array_size; i++) {
        if (minimum > array[i]) {
            minimum = array[i];
        }
    }
    return minimum;
}

inline DTYPE_OUT maximum(DTYPE_OUT array[], int array_size)
{
    DTYPE_OUT maximum = array[0];
    for(int i = 1; i < array_size; i++) {
        if (maximum < array[i]) {
            maximum = array[i];
        }
    }
    return maximum;
}

__kernel void mean_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = average(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}


__kernel void mean_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copyNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = average(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void mean_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny * Nz;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  array_size = copyVolumeNeighborhoodToArray(src, array, coord, Nx, Ny, Nz);

  DTYPE_OUT res = average(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void median_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copyNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}


__kernel void median_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny * Nz;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  array_size = copyVolumeNeighborhoodToArray(src, array, coord, Nx, Ny, Nz);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void median_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = median(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void minimum_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = minimum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}


__kernel void minimum_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copyNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = minimum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void minimum_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny * Nz;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  array_size = copyVolumeNeighborhoodToArray(src, array, coord, Nx, Ny, Nz);

  DTYPE_OUT res = minimum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void maximum_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = maximum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}


__kernel void maximum_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copyNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = maximum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}

__kernel void maximum_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
  const int Nx, const int Ny, const int Nz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny * Nz;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  array_size = copyVolumeNeighborhoodToArray(src, array, coord, Nx, Ny, Nz);

  DTYPE_OUT res = maximum(array, array_size);
  WRITE_IMAGE(dst, coord, res);
}