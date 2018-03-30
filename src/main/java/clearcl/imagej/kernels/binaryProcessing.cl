
__kernel void erode_6_neighborhood_3d(__read_only    image3d_t  src,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = READ_IMAGE(src, pos).x;
  if (value > 0) {
    value = READ_IMAGE(src, pos + (int4){1, 0, 0, 0}).x;
    if (value > 0) {
      value = READ_IMAGE(src, pos + (int4){-1, 0, 0, 0}).x;
      if (value > 0) {
        value = READ_IMAGE(src, pos + (int4){0, 1, 0, 0}).x;
        if (value > 0) {
          value = READ_IMAGE(src, pos + (int4){0, -1, 0, 0}).x;
          if (value > 0) {
            value = READ_IMAGE(src, pos + (int4){0, 0, 1, 0}).x;
            if (value > 0) {
              value = READ_IMAGE(src, pos + (int4){0, 0, -1, 0}).x;
            }
          }
        }
      }
    }
  }

  WRITE_IMAGE (dst, pos, value);
}

__kernel void dilate_6_neighborhood_3d(__read_only    image3d_t  src,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = READ_IMAGE(src, pos).x;
  if (value < 1) {

    value = READ_IMAGE(src, pos + (int4){1, 0, 0, 0}).x;
    if (value < 1) {
      value = READ_IMAGE(src, pos + (int4){-1, 0, 0, 0}).x;
      if (value < 1) {
        value = READ_IMAGE(src, pos + (int4){0, 1, 0, 0}).x;
        if (value < 1) {
          value = READ_IMAGE(src, pos + (int4){0, -1, 0, 0}).x;
          if (value < 1) {
            value = READ_IMAGE(src, pos + (int4){0, 0, 1, 0}).x;
            if (value < 1) {
              value = READ_IMAGE(src, pos + (int4){0, 0, -1, 0}).x;
            }
          }
        }
      }
    }
  }

  WRITE_IMAGE (dst, pos, value);
}

