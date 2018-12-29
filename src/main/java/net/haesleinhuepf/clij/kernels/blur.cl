// Adapted from Uwe Schmidt, https://github.com/ClearControl/FastFuse/blob/master/src/fastfuse/tasks/kernels/blur.cl
//

__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void gaussian_blur_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
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
        res += h * (float)READ_IMAGE_3D(src,sampler,coord+(int4)(x,y,z,0)).x;
        hsum += h;
      }
    }
  }

  res /= hsum;
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}


__kernel void gaussian_blur_slicewise_image3d
(
  DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src,
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
      res += h * (float)READ_IMAGE_3D(src,sampler,coord+(int4)(x,y,0,0)).x;
      hsum += h;
    }
  }

  res /= hsum;
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}

__kernel void gaussian_blur_image2d
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny,
  const float sx, const float sy
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  // centers
  const int2   c = (int2)  ( (Nx-1)/2, (Ny-1)/2 );
  // normalizations
  const float2 n = (float2)( -2*sx*sx, -2*sy*sy);

  float res = 0, hsum = 0;

  for (int x = -c.x; x <= c.x; x++) {
    const float wx = (x*x) / n.x;
    for (int y = -c.y; y <= c.y; y++) {
      const float wy = (y*y) / n.y;
      const float h = exp(wx + wy);
      res += h * (float)READ_IMAGE_2D(src,sampler,coord+(int2)(x,y)).x;
      hsum += h;
    }
  }

  res /= hsum;
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}

__kernel void gaussian_blur_image2d_ij
(
  DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src,
  const int Nx, const int Ny,
  const float sx, const float sy
)
{
  const int i = get_global_id(0), j = get_global_id(1);
  const int2 coord = (int2)(i,j);

  // centers
  const int2   c = (int2)  ( (Nx-1)/2, (Ny-1)/2 );
  // normalizations
  const float2 n = (float2)( -2*sx*sx, -2*sy*sy);

  float res = 0, hsum = 0;

  for (int x = -c.x; x <= c.x; x++) {
    const float wx = (x*x) / n.x;
    for (int y = -c.y; y <= c.y; y++) {
      const float wy = (y*y) / n.y;
      const float h = exp(wx + wy);
      res += h * (float)READ_IMAGE_2D(src,sampler,coord+(int2)(x,y)).x;
      hsum += h;
    }
  }

  res /= hsum;
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)res);
}

__kernel void gaussian_blur_sep_image3d
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
  // normalization
  const float n = -2*s*s;

  float res = 0, hsum = 0;
  for (int v = -c; v <= c; v++) {
    const float h = exp((v*v)/n);
    res += h * (float)READ_IMAGE_3D(src,sampler,coord+v*dir).x;
    hsum += h;
  }
  res /= hsum;
  WRITE_IMAGE_3D(dst,coord,(DTYPE_OUT)res);
}


__kernel void gaussian_blur_sep_image2d
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
  // normalization
  const float n = -2*s*s;

  float res = 0, hsum = 0;
  for (int v = -c; v <= c; v++) {
    const float h = exp((v*v)/n);
    res += h * (float)READ_IMAGE_2D(src,sampler,coord+v*dir).x;
    hsum += h;
  }
  res /= hsum;
  WRITE_IMAGE_2D(dst,coord,(DTYPE_OUT)(res));
}


