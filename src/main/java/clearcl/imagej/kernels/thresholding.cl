
__kernel void apply_threshold_3d(DTYPE_IMAGE_IN_3D  src,
                                 const    float      threshold,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float inputValue = READ_IMAGE(src, pos).x;
  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}

__kernel void apply_threshold_2d(DTYPE_IMAGE_IN_2D  src,
                                 const    float      threshold,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float inputValue = READ_IMAGE(src, pos).x;
  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}


__kernel void apply_local_threshold_3d(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_3D local_threshold,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float inputValue = READ_IMAGE(src, pos).x;
  const float threshold = READ_IMAGE(local_threshold, pos).x;

  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}

__kernel void apply_local_threshold_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D local_threshold,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float inputValue = READ_IMAGE(src, pos).x;
  const float threshold = READ_IMAGE(local_threshold, pos).x;
  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}
