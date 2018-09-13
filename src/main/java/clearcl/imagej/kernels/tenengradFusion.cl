__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void tenengrad_fusion_with_provided_weights_2_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_with_provided_weights_3_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}



__kernel void tenengrad_fusion_with_provided_weights_4_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_with_provided_weights_5_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_with_provided_weights_6_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_with_provided_weights_7_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}


__kernel void tenengrad_fusion_with_provided_weights_8_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6, DTYPE_IMAGE_IN_3D src7,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6, DTYPE_IMAGE_IN_3D weight7
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;
  float w7 = read_imagef(weight7,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_IMAGE(src7,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_with_provided_weights_9_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6, DTYPE_IMAGE_IN_3D src7, DTYPE_IMAGE_IN_3D src8,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6, DTYPE_IMAGE_IN_3D weight7, DTYPE_IMAGE_IN_3D weight8
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;
  float w7 = read_imagef(weight7,sampler_weight,coord_weight).x;
  float w8 = read_imagef(weight8,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;
  w8 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_IMAGE(src7,sampler,coord).x;
  const float  v8 = (float)READ_IMAGE(src8,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7 + w8 * v8;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_with_provided_weights_10_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6, DTYPE_IMAGE_IN_3D src7, DTYPE_IMAGE_IN_3D src8, DTYPE_IMAGE_IN_3D src9,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6, DTYPE_IMAGE_IN_3D weight7, DTYPE_IMAGE_IN_3D weight8, DTYPE_IMAGE_IN_3D weight9
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;
  float w7 = read_imagef(weight7,sampler_weight,coord_weight).x;
  float w8 = read_imagef(weight8,sampler_weight,coord_weight).x;
  float w9 = read_imagef(weight9,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + w9 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;
  w8 /= wsum;
  w9 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_IMAGE(src7,sampler,coord).x;
  const float  v8 = (float)READ_IMAGE(src8,sampler,coord).x;
  const float  v9 = (float)READ_IMAGE(src9,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7 + w8 * v8 + w9 * v9;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_with_provided_weights_11_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6, DTYPE_IMAGE_IN_3D src7, DTYPE_IMAGE_IN_3D src8, DTYPE_IMAGE_IN_3D src9, DTYPE_IMAGE_IN_3D src10,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6, DTYPE_IMAGE_IN_3D weight7, DTYPE_IMAGE_IN_3D weight8, DTYPE_IMAGE_IN_3D weight9, DTYPE_IMAGE_IN_3D weight10
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;
  float w7 = read_imagef(weight7,sampler_weight,coord_weight).x;
  float w8 = read_imagef(weight8,sampler_weight,coord_weight).x;
  float w9 = read_imagef(weight9,sampler_weight,coord_weight).x;
  float w10 = read_imagef(weight9,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + w9 + w10 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;
  w8 /= wsum;
  w9 /= wsum;
  w10 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_IMAGE(src7,sampler,coord).x;
  const float  v8 = (float)READ_IMAGE(src8,sampler,coord).x;
  const float  v9 = (float)READ_IMAGE(src9,sampler,coord).x;
  const float  v10 = (float)READ_IMAGE(src10,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7 + w8 * v8 + w9 * v9 + w10 * v10;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}

__kernel void tenengrad_fusion_with_provided_weights_12_images(
  DTYPE_IMAGE_OUT_3D dst, const int factor,
  DTYPE_IMAGE_IN_3D src0, DTYPE_IMAGE_IN_3D src1, DTYPE_IMAGE_IN_3D src2, DTYPE_IMAGE_IN_3D src3, DTYPE_IMAGE_IN_3D src4, DTYPE_IMAGE_IN_3D src5,
  DTYPE_IMAGE_IN_3D src6, DTYPE_IMAGE_IN_3D src7, DTYPE_IMAGE_IN_3D src8, DTYPE_IMAGE_IN_3D src9, DTYPE_IMAGE_IN_3D src10, DTYPE_IMAGE_IN_3D src11,
  DTYPE_IMAGE_IN_3D weight0, DTYPE_IMAGE_IN_3D weight1, DTYPE_IMAGE_IN_3D weight2, DTYPE_IMAGE_IN_3D weight3, DTYPE_IMAGE_IN_3D weight4, DTYPE_IMAGE_IN_3D weight5,
  DTYPE_IMAGE_IN_3D weight6, DTYPE_IMAGE_IN_3D weight7, DTYPE_IMAGE_IN_3D weight8, DTYPE_IMAGE_IN_3D weight9, DTYPE_IMAGE_IN_3D weight10, DTYPE_IMAGE_IN_3D weight11
)
{
  const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);
  const int4 coord = (int4)(i,j,k,0);

  const float4 coord_weight = (float4)((i+0.5f)/factor,(j+0.5f)/factor,k+0.5f,0);
  const sampler_t sampler_weight = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_LINEAR;

  float w0 = read_imagef(weight0,sampler_weight,coord_weight).x;
  float w1 = read_imagef(weight1,sampler_weight,coord_weight).x;
  float w2 = read_imagef(weight2,sampler_weight,coord_weight).x;
  float w3 = read_imagef(weight3,sampler_weight,coord_weight).x;
  float w4 = read_imagef(weight4,sampler_weight,coord_weight).x;
  float w5 = read_imagef(weight5,sampler_weight,coord_weight).x;
  float w6 = read_imagef(weight6,sampler_weight,coord_weight).x;
  float w7 = read_imagef(weight7,sampler_weight,coord_weight).x;
  float w8 = read_imagef(weight8,sampler_weight,coord_weight).x;
  float w9 = read_imagef(weight9,sampler_weight,coord_weight).x;
  float w10 = read_imagef(weight9,sampler_weight,coord_weight).x;
  float w11 = read_imagef(weight9,sampler_weight,coord_weight).x;

  const float wsum = w0 + w1 + w2 + w3 + w4 + w5 + w6 + w7 + w8 + w9 + w10 + w11 + 1e-30f; // add small epsilon to avoid wsum = 0
  w0 /= wsum;
  w1 /= wsum;
  w2 /= wsum;
  w3 /= wsum;
  w4 /= wsum;
  w5 /= wsum;
  w6 /= wsum;
  w7 /= wsum;
  w8 /= wsum;
  w9 /= wsum;
  w10 /= wsum;
  w11 /= wsum;

  const float  v0 = (float)READ_IMAGE(src0,sampler,coord).x;
  const float  v1 = (float)READ_IMAGE(src1,sampler,coord).x;
  const float  v2 = (float)READ_IMAGE(src2,sampler,coord).x;
  const float  v3 = (float)READ_IMAGE(src3,sampler,coord).x;
  const float  v4 = (float)READ_IMAGE(src4,sampler,coord).x;
  const float  v5 = (float)READ_IMAGE(src5,sampler,coord).x;
  const float  v6 = (float)READ_IMAGE(src6,sampler,coord).x;
  const float  v7 = (float)READ_IMAGE(src7,sampler,coord).x;
  const float  v8 = (float)READ_IMAGE(src8,sampler,coord).x;
  const float  v9 = (float)READ_IMAGE(src9,sampler,coord).x;
  const float  v10 = (float)READ_IMAGE(src10,sampler,coord).x;
  const float  v11 = (float)READ_IMAGE(src11,sampler,coord).x;
  const float res = w0 * v0 + w1 * v1 + w2 * v2 + w3 * v3 + w4 * v4 + w5 * v5 + w6 * v6 + w7 * v7 + w8 * v8 + w9 * v9 + w10 * v10 + w11 * v11;

  WRITE_IMAGE(dst,coord,(DTYPE_OUT)res);
}
