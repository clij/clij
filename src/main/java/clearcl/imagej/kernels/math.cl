
__kernel void multiplyPixelwise(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void addPixelwise(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void addWeightedPixelwise(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                                 float factor,
                                 float factor1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * factor + READ_IMAGE(src1, pos).x * factor1;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void addScalar(__read_only    image3d_t  src,
                                 float scalar,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + scalar;

  WRITE_IMAGE (dst, pos, value);
}
