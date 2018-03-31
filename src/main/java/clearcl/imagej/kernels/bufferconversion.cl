
__kernel void convert_buffer_image_3d(DTYPE_IMAGE_IN_3D     src,
                          DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  sampler_t sampler = 0;
  int4 pos = {x, y, z, 0};

  DTYPE_IN value = READ_IMAGE(src, sampler, pos).x;

  WRITE_IMAGE(dst, pos, (DTYPE_OUT)value);
}

