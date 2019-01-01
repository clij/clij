
__kernel void set_pixels_to_width_src1_3d(
DTYPE_IMAGE_IN_3D src1,
DTYPE_IMAGE_IN_3D src2,
DTYPE_IMAGE_OUT_3D  dst)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  float value = GET_IMAGE_IN_WIDTH(src1);

  WRITE_IMAGE_3D (dst, (int4)(x,y,z,0), (DTYPE_OUT)value);
}

__kernel void set_pixels_to_width_src2_3d(
DTYPE_IMAGE_IN_3D src1,
DTYPE_IMAGE_IN_3D src2,
DTYPE_IMAGE_OUT_3D  dst)
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  float value = GET_IMAGE_IN_WIDTH(src2);

  WRITE_IMAGE_3D (dst, (int4)(x,y,z,0), (DTYPE_OUT)value);
}

