__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;



__kernel void reslice_bottom_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(0);
  const int sy = get_global_id(2);
  const int sz = get_global_id(1);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_size(2) - get_global_id(2) - 1;

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void reslice_left_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(2);
  const int sy = get_global_id(0);
  const int sz = get_global_id(1);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}


__kernel void reslice_right_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(2);
  const int sy = get_global_id(0);
  const int sz = get_global_id(1);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_size(2) - get_global_id(2) - 1;

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}


__kernel void reslice_top_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int sx = get_global_id(0);
  const int sy = get_global_id(2);
  const int sz = get_global_id(1);

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}
