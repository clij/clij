__kernel void downsample_3d_nearest(write_only image3d_t dst, read_only image3d_t src, float factor_x, float factor_y, float factor_z) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const int sx = factor_x * dx;
  const int sy = factor_y * dy;
  const int sz = factor_z * dz;
  const DTYPE_IN out = READ_IMAGE(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void downsample_2d_nearest(write_only image2d_t dst, read_only image2d_t src, float factor_x, float factor_y) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int sx = factor_x * dx;
  const int sy = factor_y * dy;
  const DTYPE_IN out = READ_IMAGE(src,sampler,(int2)(sx,sy)).x;
  WRITE_IMAGE(dst,(int2)(dx,dy),(DTYPE_OUT)out);
}

#define SIZEX 4
#define SIZEY 4
#define SIZETotal SIZEX * SIZEY

__kernel void DownSample (  __read_only image2d_t src,
                                                 __write_only image2d_t des,
                                                 __constant int * dim // "constant" memory (64K) pre-cached so it can be read fast
                                )
{
    // tell the sampler how to read the image
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_FILTER_NEAREST;

    int i = get_global_id (0);
    int j = get_global_id (1);

   // jump to the starting indexes
   int is = i * SIZEX;
   int js = j * SIZEY;

   if ( is >= dim[0] - SIZEX && js >= dim[1] - SIZEY )
       return;

   uint4 total = (uint4) 0;

   // we can unroll the loop to reduce overhead (e.g., condition check)
   // if you know the size (e.g., 4 * 4 ), then you can write 16 separate
   // "read_imageui" instead of using the loop
   // There is a way for CPU to generate the unrolled code to be
   // placed in the kernel.
   for ( int z = 0; z < SIZEX; z++ ) {
       for ( int x = 0; x < SIZEX; x++ ) {
            for ( int y = 0; y < SIZEY; y++ ) {
                if ( is < dim[0] - x && js < dim[1] - y ) {
                    total += read_imageui ( src, sampler, (int2) ( is + x, js + y ) );
                }
            }
        }
    }

    total = (uint4) ( total / SIZETotal );
    write_imageui ( des, (int2) ( i, j ), total );
}


// the following two methods originate from
// https://github.com/ClearControl/fastfuse/blob/master/src/main/java/fastfuse/tasks/kernels/downsampling.cl

#define min_nobranch(x,y) x < y ? x : y;
#define max_nobranch(x,y) x > y ? x : y;

inline void swap(DTYPE_IN *a, int i, int j) {
  DTYPE_IN t;
  t    = min_nobranch(a[i],a[j]);
  a[j] = max_nobranch(a[i],a[j]);
  a[i] = t;
}


__kernel void downsample_xy_by_half_median(write_only image3d_t dst, read_only image3d_t src) {

  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord_out = (int4)(i,j,k,0);
  const int x = 2*i, y = 2*j, z = k;

  DTYPE_IN pixel[4];
  pixel[0] = READ_IMAGE(src,sampler,(int4)(x+0,y+0,z,0)).x;
  pixel[1] = READ_IMAGE(src,sampler,(int4)(x+0,y+1,z,0)).x;
  pixel[2] = READ_IMAGE(src,sampler,(int4)(x+1,y+0,z,0)).x;
  pixel[3] = READ_IMAGE(src,sampler,(int4)(x+1,y+1,z,0)).x;

  // // sort pixel array
  // swap(pixel,0,1);
  // swap(pixel,2,3);
  // swap(pixel,0,2);
  // swap(pixel,1,3);
  // swap(pixel,1,2);

  // if ((pixel[0] > pixel[1]) ||
  //     (pixel[1] > pixel[2]) ||
  //     (pixel[2] > pixel[3]))
  //   printf("array not sorted for i=%d, j=%d, k=%d\n", i,j,k);

  // swap array elements such that pixel[0] = "min(pixel)" and pixel[3] = "max(pixel)"
  // this tiny performance improvement is only there to make Martin happy
  swap(pixel,0,1);
  swap(pixel,2,3);
  swap(pixel,0,2);
  swap(pixel,1,3);

  // if ( (pixel[0] > pixel[1]) || (pixel[0] > pixel[2]) || (pixel[0] > pixel[3]) ||
  //      (pixel[3] < pixel[2]) || (pixel[3] < pixel[1]) || (pixel[3] < pixel[0]) )
  //   printf("array not sorted for i=%d, j=%d, k=%d\n", i,j,k);

  // output is mean of medians
  const float out = (pixel[1] + pixel[2]) / 2.0f;

  WRITE_IMAGE(dst,coord_out,(DTYPE_OUT)out);
}





















