__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;



__kernel void rotate_left_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_size(1) - get_global_id(1) - 1;
  const int sy = get_global_id(0);
  const int sz = get_global_id(2);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void rotate_right_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(1);
  const int sy = get_global_size(0) - get_global_id(0) - 1;
  const int sz = get_global_id(2);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void rotate_left_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_size(1) - get_global_id(1) - 1;
  const int sy = get_global_id(0);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,(int2)(sx,sy)).x;
  WRITE_IMAGE_2D(dst,(int2)(dx,dy),(DTYPE_OUT)out);
}


__kernel void rotate_right_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(1);
  const int sy = get_global_size(0) - get_global_id(0) - 1;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,(int2)(sx,sy)).x;
  WRITE_IMAGE_2D(dst,(int2)(dx,dy),(DTYPE_OUT)out);
}