__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__constant float hx[] = {-1,-2,-1,-2,-4,-2,-1,-2,-1,0,0,0,0,0,0,0,0,0,1,2,1,2,4,2,1,2,1};
__constant float hy[] = {-1,-2,-1,0,0,0,1,2,1,-2,-4,-2,0,0,0,2,4,2,-1,-2,-1,0,0,0,1,2,1};
__constant float hz[] = {-1,0,1,-2,0,2,-1,0,1,-2,0,2,-4,0,4,-2,0,2,-1,0,1,-2,0,2,-1,0,1};


inline float sobel_magnitude_squared(DTYPE_IMAGE_IN_3D src, const int i0, const int j0, const int k0) {
  float Gx = 0.0f, Gy = 0.0f, Gz = 0.0f;
  for (int i = 0; i < 3; ++i) for (int j = 0; j < 3; ++j) for (int k = 0; k < 3; ++k) {
    const int dx = i-1, dy = j-1, dz = k-1;
    const int ind = i + 3*j + 3*3*k;
    const float pix = (float)READ_IMAGE(src,sampler,(int4)(i0+dx,j0+dy,k0+dz,0)).x;
    Gx += hx[ind]*pix;
    Gy += hy[ind]*pix;
    Gz += hz[ind]*pix;
  }
  return Gx*Gx + Gy*Gy + Gz*Gz;
}


__kernel void tenengrad_weight_unnormalized(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  float w = sobel_magnitude_squared(src,i,j,k);
  // w = w*w;
  WRITE_IMAGE(dst,coord,(DTYPE_OUT)w);
}

inline float sobel_magnitude_squared_slice_wise(DTYPE_IMAGE_IN_3D src, const int i0, const int j0, const int k0) {
  float Gx = 0.0f, Gy = 0.0f;
  for (int i = 0; i < 3; ++i) for (int j = 0; j < 3; ++j) for (int k = 0; k < 3; ++k) {
    const int dx = i-1, dy = j-1, dz = k-1;
    const int ind = i + 3*j + 3*3*k;
    const float pix = (float)READ_IMAGE(src,sampler,(int4)(i0+dx,j0+dy,k0+dz,0)).x;
    Gx += hx[ind]*pix;
    Gy += hy[ind]*pix;
  }
  return Gx*Gx + Gy*Gy;
}


__kernel void tenengrad_weight_unnormalized_slice_wise(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);
  float w = sobel_magnitude_squared_slice_wise(src,i,j,k);
  // w = w*w;
  WRITE_IMAGE(dst,coord,(DTYPE_OUT)w);
}


