__kernel void copy_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE(src,sampler,(int4)(dx,dy,dz,0)).x;
  WRITE_IMAGE(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void copy_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const DTYPE_IN out = READ_IMAGE(src,sampler,(int2)(dx,dy)).x;
  WRITE_IMAGE(dst,(int2)(dx,dy),(DTYPE_OUT)out);
}

__kernel void copySlice(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_3D src, int slice) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const DTYPE_IN out = READ_IMAGE(src,sampler,(int4)(dx,dy,slice,0)).x;
  WRITE_IMAGE(dst,(int2)(dx,dy),(DTYPE_OUT)out);
}


__kernel void crop(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src, int start_x, int start_y, int start_z) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const int sx = start_x + dx;
  const int sy = start_y + dy;
  const int sz = start_z + dz;
  const DTYPE_IN out = READ_IMAGE(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}