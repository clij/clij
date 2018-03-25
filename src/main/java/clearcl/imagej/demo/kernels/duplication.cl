__kernel void duplicate(__read_only    image3d_t  src,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x;

  WRITE_IMAGE (dst, pos, value);
}
