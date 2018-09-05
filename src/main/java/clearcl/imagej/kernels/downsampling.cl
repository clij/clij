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























