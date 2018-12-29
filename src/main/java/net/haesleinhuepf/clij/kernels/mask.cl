__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void mask_3d(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_3D  mask,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = 0;
  if (READ_IMAGE_3D(mask, sampler, pos).x != 0) {
    value = READ_IMAGE_3D(src, sampler, pos).x;
  }

  WRITE_IMAGE_3D (dst, pos, value);
}


__kernel void mask_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  mask,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  DTYPE_OUT value = 0;
  if (READ_IMAGE_2D(mask, sampler, pos).x != 0) {
    value = READ_IMAGE_2D(src, sampler, pos).x;
  }

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void maskStackWithPlane(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_2D  mask,
                         DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};
  const int2 pos2d = (int2){x,y};

  DTYPE_OUT value = 0;
  if (READ_IMAGE_2D(mask, sampler, pos2d).x != 0) {
    value = READ_IMAGE_3D(src, sampler, pos3d).x;
  }

  WRITE_IMAGE_3D (dst, pos3d, value);
}