__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void split_2_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(const int4)(i,j,2*k,0)).x);
   WRITE_IMAGE(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,2*k+1,0)).x);
}

__kernel void split_3_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(const int4)(i,j,3*k,0)).x);
   WRITE_IMAGE(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,3*k+1,0)).x);
   WRITE_IMAGE(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,3*k+2,0)).x);
}

__kernel void split_4_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2, DTYPE_IMAGE_OUT_3D dst3){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(const int4)(i,j,4*k,0)).x);
   WRITE_IMAGE(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,4*k+1,0)).x);
   WRITE_IMAGE(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,4*k+2,0)).x);
   WRITE_IMAGE(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,4*k+3,0)).x);
}

__kernel void split_5_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2, DTYPE_IMAGE_OUT_3D dst3, DTYPE_IMAGE_OUT_3D dst4){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(const int4)(i,j,5*k,0)).x);
   WRITE_IMAGE(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,5*k+1,0)).x);
   WRITE_IMAGE(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,5*k+2,0)).x);
   WRITE_IMAGE(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,5*k+3,0)).x);
   WRITE_IMAGE(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE(src,sampler,(int4)(i,j,5*k+3,0)).x);
}
