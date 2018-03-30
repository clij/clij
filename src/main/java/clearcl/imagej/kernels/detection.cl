
__kernel void detect_local_optima_3d(
        __read_only image3d_t src,
        __write_only image3d_t dst,
        __private int radius,
        __private int detect_maxima
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float localMin = READ_IMAGE(src, sampler, pos).x + 1;
    float localMax = localMin - 2;
    int4 localMinPos = pos;
    int4 localMaxPos = pos;

    for(int x = -radius; x < radius + 1; x++)
    {
        for(int y = -radius; y < radius + 1; y++)
        {
            for(int z = -radius; z < radius + 1; z++)
            {
                const int4 localPos = pos + (int4){ x, y, z, 0};

                float value = READ_IMAGE(src, sampler, localPos).x;

                if (value < localMin) {
                    localMin = value;
                    localMinPos = localPos;
                }
                if (value > localMax) {
                    localMax = value;
                    localMaxPos = localPos;
                }
            }
        }
    }

    if ((detect_maxima == 1 && pos.x == localMaxPos.x && pos.y == localMaxPos.y && pos.z == localMaxPos.z) ||
        (pos.x == localMinPos.x && pos.y == localMinPos.y && pos.z == localMinPos.z)) {
        WRITE_IMAGE(dst, pos, (DTYPE_OUT){1, 0, 0, 0});
    } else {
        WRITE_IMAGE(dst, pos, (DTYPE_OUT){0, 0, 0, 0});
    }
}