
__kernel void detect_local_optima_3d(
        DTYPE_IMAGE_IN_3D src,
        DTYPE_IMAGE_OUT_3D dst,
        __private int radius,
        __private int detect_maxima
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float localMin = READ_IMAGE_3D(src, sampler, pos).x + 1;
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

                float value = READ_IMAGE_3D(src, sampler, localPos).x;

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
        WRITE_IMAGE_3D(dst, pos, ((DTYPE_OUT){1, 0, 0, 0}));
    } else {
        WRITE_IMAGE_3D(dst, pos, ((DTYPE_OUT){0, 0, 0, 0}));
    }
}


__kernel void detect_local_optima_3d_slice_by_slice(
        DTYPE_IMAGE_IN_3D src,
        DTYPE_IMAGE_OUT_3D dst,
        __private int radius,
        __private int detect_maxima
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int4 pos = {get_global_id(0), get_global_id(1), get_global_id(2), 0};
    float localMin = READ_IMAGE_3D(src, sampler, pos).x + 1;
    float localMax = localMin - 2;
    int4 localMinPos = pos;
    int4 localMaxPos = pos;

    for(int x = -radius; x < radius + 1; x++)
    {
        for(int y = -radius; y < radius + 1; y++)
        {
            int z = 0;
            const int4 localPos = pos + (int4){ x, y, z, 0};

            float value = READ_IMAGE_3D(src, sampler, localPos).x;

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

    if ((detect_maxima == 1 && pos.x == localMaxPos.x && pos.y == localMaxPos.y) ||
        (pos.x == localMinPos.x && pos.y == localMinPos.y)) {
        WRITE_IMAGE_3D(dst, pos, ((DTYPE_OUT){1, 0, 0, 0}));
    } else {
        WRITE_IMAGE_3D(dst, pos, ((DTYPE_OUT){0, 0, 0, 0}));
    }
}

__kernel void detect_local_optima_2d(
        DTYPE_IMAGE_IN_2D src,
        DTYPE_IMAGE_OUT_2D dst,
        __private int radius,
        __private int detect_maxima
)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

    int2 pos = {get_global_id(0), get_global_id(1)};
    float localMin = READ_IMAGE_2D(src, sampler, pos).x + 1;
    float localMax = localMin - 2;
    int2 localMinPos = pos;
    int2 localMaxPos = pos;

    for(int x = -radius; x < radius + 1; x++)
    {
        for(int y = -radius; y < radius + 1; y++)
        {
            const int2 localPos = pos + (int2){ x, y};

            float value = READ_IMAGE_2D(src, sampler, localPos).x;

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

    if ((detect_maxima == 1 && pos.x == localMaxPos.x && pos.y == localMaxPos.y) ||
        (pos.x == localMinPos.x && pos.y == localMinPos.y)) {
        WRITE_IMAGE_2D(dst, pos, ((DTYPE_OUT)1));
    } else {
        WRITE_IMAGE_2D(dst, pos, ((DTYPE_OUT)0));
    }
}