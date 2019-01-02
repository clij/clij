__kernel void copy_3d (DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);

  const int4 pos = (int4){dx,dy,dz,0};

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,pos).x;
  WRITE_IMAGE_3D(dst, pos,(DTYPE_OUT)out);
}

__kernel void copy_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);


  const int2 pos = (int2){dx,dy};

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,pos).x;
  WRITE_IMAGE_2D(dst,pos,(DTYPE_OUT)out);
}

__kernel void copySlice(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_3D src, int slice) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int4 pos4 = (int4){dx,dy,slice,0};
  const int2 pos2 = (int2){dx,dy};

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,pos4).x;
  WRITE_IMAGE_2D(dst,pos2,(DTYPE_OUT)out);
}

__kernel void putSliceInStack(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_2D src, int slice) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int2 pos2 = (int2){dx,dy};
  const int4 pos4 = (int4){dx,dy,slice,0};

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,pos2).x;
  WRITE_IMAGE_3D(dst,pos4,(DTYPE_OUT)out);
}


__kernel void crop_3d(DTYPE_IMAGE_OUT_3D dst, DTYPE_IMAGE_IN_3D src, int start_x, int start_y, int start_z) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);
  const int dz = get_global_id(2);


  const int sx = start_x + dx;
  const int sy = start_y + dy;
  const int sz = start_z + dz;

  const int4 dpos = (int4){dx,dy,dz,0};
  const int4 spos = (int4){sx,sy,sz,0};

  const DTYPE_IN out = READ_IMAGE_3D(src,sampler,spos).x;
  WRITE_IMAGE_3D(dst,dpos,(DTYPE_OUT)out);
}


__kernel void crop_2d(DTYPE_IMAGE_OUT_2D dst, DTYPE_IMAGE_IN_2D src, int start_x, int start_y) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int dx = get_global_id(0);
  const int dy = get_global_id(1);

  const int sx = start_x + dx;
  const int sy = start_y + dy;

  const int2 dpos = (int2){dx,dy};
  const int2 spos = (int2){sx,sy};

  const DTYPE_IN out = READ_IMAGE_2D(src,sampler,spos).x;
  WRITE_IMAGE_2D(dst,dpos,(DTYPE_OUT)out);
}

