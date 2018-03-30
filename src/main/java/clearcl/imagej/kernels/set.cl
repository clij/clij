
__kernel void set_3d(__write_only    image3d_t  dst,
                  float value
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  WRITE_IMAGE (dst, (int4)(x,y,z,0), (DTYPE_OUT)value);
}