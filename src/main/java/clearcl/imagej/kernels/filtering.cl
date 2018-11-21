
__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;




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
  const int4 coord = (int4)(i,j,k,0);

  int array_size = Nx * Ny;
  DTYPE_OUT array[MAX_ARRAY_SIZE];

  copySliceNeighborhoodToArray(src, array, coord, Nx, Ny);

  DTYPE_OUT res = average(array, array_size);
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

