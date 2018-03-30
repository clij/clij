// Adapted from Uwe Schmidt, https://github.com/ClearControl/FastFuse/blob/master/src/fastfuse/tasks/kernels/blur.cl
//

__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void gaussian_blur_image3d
(
  write_only image3d_t dst, read_only image3d_t src,
  const int Nx, const int Ny, const int Nz,
  const float sx, const float sy, const float sz
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  // centers
  const int4   c = (int4)  ( (Nx-1)/2, (Ny-1)/2, (Nz-1)/2, 0 );
  // normalizations
  const float4 n = (float4)( -2*sx*sx, -2*sy*sy, -2*sz*sz, 0 );

  float res = 0, hsum = 0;

  for (int x = -c.x; x <= c.x; x++) {
    const float wx = (x*x) / n.x;
    for (int y = -c.y; y <= c.y; y++) {
      const float wy = (y*y) / n.y;
      for (int z = -c.z; z <= c.z; z++) {
        const float wz = (z*z) / n.z;
        const float h = exp(wx + wy + wz);
        res += h * (float)READ_IMAGE(src,sampler,coord+(int4)(x,y,z,0)).x;
        hsum += h;
      }
    }
  }

  res /= hsum;
  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void gaussian_blur_slicewise_image3d
(
  write_only image3d_t dst, read_only image3d_t src,
  const int Nx, const int Ny,
  const float sx, const float sy
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  // centers
  const int4   c = (int4)  ( (Nx-1)/2, (Ny-1)/2, 0, 0 );
  // normalizations
  const float4 n = (float4)( -2*sx*sx, -2*sy*sy, 0, 0 );

  float res = 0, hsum = 0;

  for (int x = -c.x; x <= c.x; x++) {
    const float wx = (x*x) / n.x;
    for (int y = -c.y; y <= c.y; y++) {
      const float wy = (y*y) / n.y;
      const float h = exp(wx + wy);
      res += h * (float)READ_IMAGE(src,sampler,coord+(int4)(x,y,0,0)).x;
      hsum += h;
    }
  }

  res /= hsum;
  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}
