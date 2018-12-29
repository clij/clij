__kernel void sum_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN sum = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    sum = sum + READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y),(DTYPE_OUT)sum);
}

__kernel void mean_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN sum = 0;
  int count = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    sum = sum + READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    count++;
  }
  WRITE_IMAGE_2D(dst,(int2)(x,y),(DTYPE_OUT)(sum / count));
}

__kernel void arg_max_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_OUT_2D dst_arg,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN max = 0;
  int max_pos = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if (value > max || z == 0) {
      max = value;
      max_pos = z;
    }
  }
  WRITE_IMAGE_2D(dst_max,(int2)(x,y),(DTYPE_OUT)max);
  WRITE_IMAGE_2D(dst_arg,(int2)(x,y),(DTYPE_OUT)max_pos);
}

__kernel void max_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN max = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if (value > max || z == 0) {
      max = value;
    }
  }
  WRITE_IMAGE_2D(dst_max,(int2)(x,y),(DTYPE_OUT)max);
}

__kernel void min_project_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_min,
    DTYPE_IMAGE_IN_3D src
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  DTYPE_IN min = 0;
  for(int z = 0; z < GET_IMAGE_IN_DEPTH(src); z++)
  {
    DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(x,y,z,0)).x;
    if (value < min || z == 0) {
      min = value;
    }
  }
  WRITE_IMAGE_2D(dst_min,(int2)(x,y),(DTYPE_OUT)min);
}


__kernel void max_project_dim_select_3d_2d(
    DTYPE_IMAGE_OUT_2D dst_max,
    DTYPE_IMAGE_IN_3D src,
    int projection_x,
    int projection_y,
    int projection_dim
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  int4 sourcePos = (int4)(0,0,0,0);
  int2 targetPos = (int2)(get_global_id(0), get_global_id(1));

  if (projection_x == 0) {
    sourcePos.x = get_global_id(0);
  } else if (projection_x == 1) {
    sourcePos.y = get_global_id(0);
  } else {
    sourcePos.z = get_global_id(0);
  }
  if (projection_y == 0) {
    sourcePos.x = get_global_id(1);
  } else if (projection_y == 1) {
    sourcePos.y = get_global_id(1);
  } else {
    sourcePos.z = get_global_id(1);
  }


  int max_d = 0;
  if (projection_dim == 0) {
    max_d = GET_IMAGE_IN_WIDTH(src);
  } else if (projection_dim == 1) {
    max_d = GET_IMAGE_IN_HEIGHT(src);
  } else {
    max_d = GET_IMAGE_IN_DEPTH(src);
  }

  DTYPE_IN max = 0;
  for(int d = 0; d < max_d; d++)
  {
    if (projection_dim == 0) {
      sourcePos.x = d;
    } else if (projection_dim == 0) {
      sourcePos.y = d;
    } else {
      sourcePos.z = d;
    }
    DTYPE_IN value = READ_IMAGE_3D(src,sampler, sourcePos).x;
    if (value > max || d == 0) {
      max = value;
    }
  }
  WRITE_IMAGE_2D(dst_max,targetPos,(DTYPE_OUT)max);
}


__kernel void radialProjection3d(
    DTYPE_IMAGE_OUT_3D dst,
    DTYPE_IMAGE_IN_3D src,
    float deltaAngle
) {
  const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;

  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const float imageHalfWidth = GET_IMAGE_IN_WIDTH(src) / 2;
  const float imageHalfHeight = GET_IMAGE_IN_HEIGHT(src) / 2;

  float angleInRad = ((float)z) * deltaAngle / 180.0 * M_PI;
  //float maxRadius = sqrt(pow(imageHalfWidth, 2.0f) + pow(imageHalfHeight, 2.0f));
  float radius = x;

  const int sx = (int)(imageHalfWidth + sin(angleInRad) * radius);
  const int sy = (int)(imageHalfHeight + cos(angleInRad) * radius);
  const int sz = y;

  DTYPE_IN value = READ_IMAGE_3D(src,sampler,(int4)(sx,sy,sz,0)).x;
  WRITE_IMAGE_3D(dst,(int4)(x,y,z,0),(DTYPE_OUT)value);
}

