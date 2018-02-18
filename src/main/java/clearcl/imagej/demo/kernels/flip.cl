// Taken / modified from https://github.com/ClearControl/FastFuse/blob/master/src/fastfuse/tasks/kernels/flip.cl
//

__kernel void flip_ui  (    __read_only   image3d_t  src,
                           __write_only   image3d_t  dst,
                           const          int        flipx,
                           const          int        flipy,
                           const          int        flipz
                     )
{
  const sampler_t intsampler  = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int width = get_global_size(0);
  const int height = get_global_size(1);
  const int depth = get_global_size(2);

  const int4 pos = (int4){flipx?(width-1-x):x,
                          flipy?(height-1-y):y,
                          flipz?(depth-1-z):z,0};

  const ushort value = READ_IMAGE(src, intsampler, pos).x;

  WRITE_IMAGE (dst, (int4){x,y,z,0}, (uint4){value,0,0,0});
}
