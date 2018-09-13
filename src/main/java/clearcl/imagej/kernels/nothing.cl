__kernel void nothing(__read_only    image3d_t  src,
                      __write_only    image3d_t  dst
                    )
{
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const float out = read_imagef(src,sampler,(int4)(x,y,z,0)).x;
  write_imagef(dst,(int4)(x,y,z,0),out);
}