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


__kernel void tenengrad_fusion_2_images(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1) {

  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  float w1 = sobel_magnitude_squared(src0,i,j,k);
  float w2 = sobel_magnitude_squared(src1,i,j,k);

  const float wsum = w1 + w2 + 1e-30f; // add small epsilon to avoid wsum = 0
  w1 /= wsum;
  w2 /= wsum;

  const float  v1 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float res = w1*v1 + w2*v2;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_3_images(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2) {

  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  float w1 = sobel_magnitude_squared(src0,i,j,k);
  float w2 = sobel_magnitude_squared(src1,i,j,k);
  float w3 = sobel_magnitude_squared(src2,i,j,k);

  const float wsum = w1 + w2 + w3 + 1e-30f; // add small epsilon to avoid wsum = 0
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;

  const float  v1 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float res = w1*v1 + w2*v2 + w3*v3;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_4_images(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3) {

  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  float w1 = sobel_magnitude_squared(src0,i,j,k);
  float w2 = sobel_magnitude_squared(src1,i,j,k);
  float w3 = sobel_magnitude_squared(src2,i,j,k);
  float w4 = sobel_magnitude_squared(src3,i,j,k);

  const float wsum = w1 + w2 + w3 + w4 + 1e-30f; // add small epsilon to avoid wsum = 0
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;

  const float  v1 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float res = w1*v1 + w2*v2 + w3*v3 + w4*v4;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_5_images(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4) {

  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  float w1 = sobel_magnitude_squared(src0,i,j,k);
  float w2 = sobel_magnitude_squared(src1,i,j,k);
  float w3 = sobel_magnitude_squared(src2,i,j,k);
  float w4 = sobel_magnitude_squared(src3,i,j,k);
  float w5 = sobel_magnitude_squared(src4,i,j,k);

  const float wsum = w1 + w2 + w3 + w4 + w5 + 1e-30f; // add small epsilon to avoid wsum = 0
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;

  const float  v1 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float res = w1*v1 + w2*v2 + w3*v3 + w4*v4 + w5*v5;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}



