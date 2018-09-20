
__kernel void multiplyPixelwise_3d(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void multiplyPixelwise_2d(__read_only    image2d_t  src,
                                 __read_only    image2d_t  src1,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void dividePixelwise_3d(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x / READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void dividePixelwise_2d(__read_only    image2d_t  src,
                                 __read_only    image2d_t  src1,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x / READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void multiplyStackWithPlanePixelwise(__read_only    image3d_t  src,
                                 __read_only    image2d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};
  const int2 pos2d = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos3d).x * READ_IMAGE(src1, pos2d).x;

  WRITE_IMAGE (dst, pos3d, value);
}

__kernel void multiplySliceBySliceWithScalars(__read_only    image3d_t  src,
                                 __constant    float*  scalars,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos3d = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos3d).x * scalars[z];

  WRITE_IMAGE (dst, pos3d, value);
}


__kernel void addPixelwise_3d(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void addPixelwise_2d(__read_only    image2d_t  src,
                                 __read_only    image2d_t  src1,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + READ_IMAGE(src1, pos).x;

  WRITE_IMAGE (dst, pos, value);
}

__kernel void addWeightedPixelwise_3d(__read_only    image3d_t  src,
                                 __read_only    image3d_t  src1,
                                 float factor,
                                 float factor1,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * factor + READ_IMAGE(src1, pos).x * factor1;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void addWeightedPixelwise_2d(__read_only    image2d_t  src,
                                 __read_only    image2d_t  src1,
                                 float factor,
                                 float factor1,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * factor + READ_IMAGE(src1, pos).x * factor1;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void addScalar_3d(__read_only    image3d_t  src,
                                 float scalar,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + scalar;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void addScalar_2d(__read_only    image2d_t  src,
                                 float scalar,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x + scalar;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void multiplyScalar_3d(__read_only    image3d_t  src,
                                 float scalar,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * scalar;

  WRITE_IMAGE (dst, pos, value);
}


__kernel void multiplyScalar_2d(__read_only    image2d_t  src,
                                 float scalar,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = READ_IMAGE(src, pos).x * scalar;

  WRITE_IMAGE (dst, pos, value);
}



__kernel void absolute_3d(DTYPE_IMAGE_IN_3D  src,
                          DTYPE_IMAGE_OUT_3D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  DTYPE_OUT value = READ_IMAGE(src, pos).x;
  if ( value < 0 ) {
    value = -1 * value;
  }

  WRITE_IMAGE (dst, pos, value);
}


__kernel void absolute_2d(DTYPE_IMAGE_IN_2D  src,
                          DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  DTYPE_OUT value = READ_IMAGE(src, pos).x;
  if ( value < 0 ) {
    value = -1 * value;
  }

  WRITE_IMAGE (dst, pos, value);
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

  const DTYPE_OUT value = max(READ_IMAGE(src, pos).x, READ_IMAGE(src1, pos).x);

  WRITE_IMAGE (dst, pos, value);
}

__kernel void maxPixelwise_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_IN_2D  src1,
                              DTYPE_IMAGE_OUT_2D  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = max(READ_IMAGE(src, pos).x, READ_IMAGE(src1, pos).x);

  WRITE_IMAGE (dst, pos, value);
}



__kernel void power_2d(DTYPE_IMAGE_IN_2D  src,
                              DTYPE_IMAGE_OUT_2D  dst,
                              float exponent
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const DTYPE_OUT value = pow(READ_IMAGE(src, pos).x, exponent);

  WRITE_IMAGE (dst, pos, value);
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

  const DTYPE_OUT value = pow(READ_IMAGE(src, pos).x, exponent);

  WRITE_IMAGE (dst, pos, value);
}



