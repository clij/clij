__kernel void copy(write_only image3d_t dst, read_only image3d_t src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const DTYPE_IN out = READ_IMAGE(src,sampler,(int4)(dx,dy,dz,0)).x;
  WRITE_IMAGE(dst,(int4)(dx,dy,dz,0),(DTYPE_OUT)out);
}

__kernel void crop(write_only image3d_t dst, read_only image3d_t src, int start_x, int start_y, int start_z) {
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