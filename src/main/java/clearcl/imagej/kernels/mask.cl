
__kernel void mask(__read_only    image3d_t  src,
                                 __read_only    image3d_t  mask,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = 0;
  if (READ_IMAGE(mask, pos).x != 0) {
    value = READ_IMAGE(src, pos).x;
  }

  WRITE_IMAGE (dst, pos, value);
}

__kernel void maskStackWithPlane(__read_only    image3d_t  src,
                                 __read_only    image2d_t  mask,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int2 pos2d = (int2){x,y};

  DTYPE_OUT value = 0;
  if (READ_IMAGE(mask, pos2d).x != 0) {
    const int4 pos3d = (int4){x,y,z,0};
    value = READ_IMAGE(src, pos3d).x;
  }

  WRITE_IMAGE (dst, pos, value);
}