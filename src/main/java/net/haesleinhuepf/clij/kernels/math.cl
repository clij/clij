__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

__kernel void multiplyPixelwise_3d(DTYPE_IMAGE_IN_3D  src,
                                   DTYPE_IMAGE_IN_3D src1,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x * READ_IMAGE_3D(src1, sampler, pos).x;

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void multiplyPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x * READ_IMAGE_2D(src1, sampler, pos).x;

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void dividePixelwise_3d(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_3D  src1,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x / READ_IMAGE_3D(src1, sampler, pos).x;

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void dividePixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x / READ_IMAGE_2D(src1, sampler, pos).x;

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void multiplyStackWithPlanePixelwise(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};
  const int2 pos2d = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos3d).x * READ_IMAGE_2D(src1, sampler, pos2d).x;

  WRITE_IMAGE_3D (dst, pos3d, value);
}

__kernel void multiplySliceBySliceWithScalars(DTYPE_IMAGE_IN_3D  src,
                                 __constant    float*  scalars,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos3d).x * scalars[z];

  WRITE_IMAGE_3D (dst, pos3d, value);
}


__kernel void addPixelwise_3d(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_3D  src1,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x + READ_IMAGE_3D(src1, sampler, pos).x;

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void addPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x + READ_IMAGE_2D(src1, sampler, pos).x;

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void addWeightedPixelwise_3d(DTYPE_IMAGE_IN_3D  src,
                                 DTYPE_IMAGE_IN_3D  src1,
                                 float factor,
                                 float factor1,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x * factor + READ_IMAGE_3D(src1, sampler, pos).x * factor1;

  WRITE_IMAGE_3D (dst, pos, value);
}


__kernel void addWeightedPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                                 DTYPE_IMAGE_IN_2D  src1,
                                 float factor,
                                 float factor1,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x * factor + READ_IMAGE_2D(src1, sampler, pos).x * factor1;

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void addScalar_3d(DTYPE_IMAGE_IN_3D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x + scalar;

  WRITE_IMAGE_3D (dst, pos, value);
}


__kernel void addScalar_2d(DTYPE_IMAGE_IN_2D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x + scalar;

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void multiplyScalar_3d(DTYPE_IMAGE_IN_3D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x * scalar;

  WRITE_IMAGE_3D (dst, pos, value);
}


__kernel void multiplyScalar_2d(DTYPE_IMAGE_IN_2D  src,
                                 float scalar,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x * scalar;

  WRITE_IMAGE_2D (dst, pos, value);
}



__kernel void absolute_3d(DTYPE_IMAGE_IN_3D  src,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = READ_IMAGE_3D(src, sampler, pos).x;
  if ( value < 0 ) {
    value = -1 * value;
  }

  WRITE_IMAGE_3D (dst, pos, value);
}


__kernel void absolute_2d(DTYPE_IMAGE_IN_2D  src,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  DTYPE_OUT value = READ_IMAGE_2D(src, sampler, pos).x;
  if ( value < 0 ) {
    value = -1 * value;
  }

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void maxPixelwise_3d(DTYPE_IMAGE_IN_3D src,
                              DTYPE_IMAGE_IN_3D src1,
                              DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_IN input = READ_IMAGE_3D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_3D(src1, sampler, pos).x;

  const DTYPE_OUT value = max(input, input1);

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void maxPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_IN_2D  src1,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_2D(src1, sampler, pos).x;

  const DTYPE_OUT value = max(input, input1);

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void minPixelwise_3d(DTYPE_IMAGE_IN_3D src,
                              DTYPE_IMAGE_IN_3D src1,
                              DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_IN input = READ_IMAGE_3D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_3D(src1, sampler, pos).x;

  const DTYPE_OUT value = min(input, input1);

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void minPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_IN_2D  src1,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = READ_IMAGE_2D(src1, sampler, pos).x;

  const DTYPE_OUT value = min(input, input1);

  WRITE_IMAGE_2D (dst, pos, value);
}

__kernel void maxPixelwiseScalar_3d(DTYPE_IMAGE_IN_3D src,
                              float valueB,
                              DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_IN input = READ_IMAGE_3D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = max(input, input1);

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void maxPixelwiseScalar_2d(DTYPE_IMAGE_IN_2D  src,
                              float valueB,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = max(input, input1);

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void minPixelwiseScalar_3d(DTYPE_IMAGE_IN_3D src,
                              float valueB,
                              DTYPE_IMAGE_OUT_3D dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_IN input = READ_IMAGE_3D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = min(input, input1);

  WRITE_IMAGE_3D (dst, pos, value);
}

__kernel void minPixelwiseScalar_2d(DTYPE_IMAGE_IN_2D  src,
                              float  valueB,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;
  const DTYPE_IN input1 = valueB;

  const DTYPE_OUT value = min(input, input1);

  WRITE_IMAGE_2D (dst, pos, value);
}



__kernel void power_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_OUT_2D  dst,
                              float exponent
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_IN input = READ_IMAGE_2D(src, sampler, pos).x;

  const DTYPE_OUT value = pow(input, exponent);

  WRITE_IMAGE_2D (dst, pos, value);
}


__kernel void power_3d(DTYPE_IMAGE_IN_3D src,
                              DTYPE_IMAGE_OUT_3D dst,
                              float exponent
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_IN input = READ_IMAGE_3D(src, sampler, pos).x;

  const DTYPE_OUT value = pow(input, exponent);

  WRITE_IMAGE_3D (dst, pos, value);
}



