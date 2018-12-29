__constant sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;


__kernel void split_2_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,2*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,2*k+1,0)).x);
}

__kernel void split_3_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,3*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,3*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,3*k+2,0)).x);
}

__kernel void split_4_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2, DTYPE_IMAGE_OUT_3D dst3){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,4*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,4*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,4*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,4*k+3,0)).x);
}

__kernel void split_5_stacks(DTYPE_IMAGE_IN_3D src, DTYPE_IMAGE_OUT_3D dst0, DTYPE_IMAGE_OUT_3D dst1, DTYPE_IMAGE_OUT_3D dst2, DTYPE_IMAGE_OUT_3D dst3, DTYPE_IMAGE_OUT_3D dst4){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,5*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,5*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,5*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,5*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,5*k+4,0)).x);
}


__kernel void split_6_stacks(DTYPE_IMAGE_IN_3D src,
                             DTYPE_IMAGE_OUT_3D dst0,
                             DTYPE_IMAGE_OUT_3D dst1,
                             DTYPE_IMAGE_OUT_3D dst2,
                             DTYPE_IMAGE_OUT_3D dst3,
                             DTYPE_IMAGE_OUT_3D dst4,
                             DTYPE_IMAGE_OUT_3D dst5
                         ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,6*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,6*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,6*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,6*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,6*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,6*k+5,0)).x);
}


__kernel void split_7_stacks(DTYPE_IMAGE_IN_3D src,
                             DTYPE_IMAGE_OUT_3D dst0,
                             DTYPE_IMAGE_OUT_3D dst1,
                             DTYPE_IMAGE_OUT_3D dst2,
                             DTYPE_IMAGE_OUT_3D dst3,
                             DTYPE_IMAGE_OUT_3D dst4,
                             DTYPE_IMAGE_OUT_3D dst5,
                             DTYPE_IMAGE_OUT_3D dst6
                         ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,7*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,7*k+6,0)).x);
}

__kernel void split_8_stacks(DTYPE_IMAGE_IN_3D src,
                                 DTYPE_IMAGE_OUT_3D dst0,
                                 DTYPE_IMAGE_OUT_3D dst1,
                                 DTYPE_IMAGE_OUT_3D dst2,
                                 DTYPE_IMAGE_OUT_3D dst3,
                                 DTYPE_IMAGE_OUT_3D dst4,
                                 DTYPE_IMAGE_OUT_3D dst5,
                                 DTYPE_IMAGE_OUT_3D dst6,
                                 DTYPE_IMAGE_OUT_3D dst7
                             ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,8*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+6,0)).x);
   WRITE_IMAGE_3D(dst7,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,8*k+7,0)).x);
}


__kernel void split_9_stacks(DTYPE_IMAGE_IN_3D src,
                                DTYPE_IMAGE_OUT_3D dst0,
                                DTYPE_IMAGE_OUT_3D dst1,
                                DTYPE_IMAGE_OUT_3D dst2,
                                DTYPE_IMAGE_OUT_3D dst3,
                                DTYPE_IMAGE_OUT_3D dst4,
                                DTYPE_IMAGE_OUT_3D dst5,
                                DTYPE_IMAGE_OUT_3D dst6,
                                DTYPE_IMAGE_OUT_3D dst7,
                                DTYPE_IMAGE_OUT_3D dst8
                            ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,9*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+6,0)).x);
   WRITE_IMAGE_3D(dst7,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+7,0)).x);
   WRITE_IMAGE_3D(dst8,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,9*k+8,0)).x);
}

__kernel void split_10_stacks(DTYPE_IMAGE_IN_3D src,
                                DTYPE_IMAGE_OUT_3D dst0,
                                DTYPE_IMAGE_OUT_3D dst1,
                                DTYPE_IMAGE_OUT_3D dst2,
                                DTYPE_IMAGE_OUT_3D dst3,
                                DTYPE_IMAGE_OUT_3D dst4,
                                DTYPE_IMAGE_OUT_3D dst5,
                                DTYPE_IMAGE_OUT_3D dst6,
                                DTYPE_IMAGE_OUT_3D dst7,
                                DTYPE_IMAGE_OUT_3D dst8,
                                DTYPE_IMAGE_OUT_3D dst9
                            ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,10*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+6,0)).x);
   WRITE_IMAGE_3D(dst7,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+7,0)).x);
   WRITE_IMAGE_3D(dst8,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+8,0)).x);
   WRITE_IMAGE_3D(dst9,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,10*k+9,0)).x);
}


__kernel void split_11_stacks(DTYPE_IMAGE_IN_3D src,
                                DTYPE_IMAGE_OUT_3D dst0,
                                DTYPE_IMAGE_OUT_3D dst1,
                                DTYPE_IMAGE_OUT_3D dst2,
                                DTYPE_IMAGE_OUT_3D dst3,
                                DTYPE_IMAGE_OUT_3D dst4,
                                DTYPE_IMAGE_OUT_3D dst5,
                                DTYPE_IMAGE_OUT_3D dst6,
                                DTYPE_IMAGE_OUT_3D dst7,
                                DTYPE_IMAGE_OUT_3D dst8,
                                DTYPE_IMAGE_OUT_3D dst9,
                                DTYPE_IMAGE_OUT_3D dst10
                            ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,11*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+6,0)).x);
   WRITE_IMAGE_3D(dst7,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+7,0)).x);
   WRITE_IMAGE_3D(dst8,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+8,0)).x);
   WRITE_IMAGE_3D(dst9,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+9,0)).x);
   WRITE_IMAGE_3D(dst10,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,11*k+10,0)).x);
}


__kernel void split_12_stacks(DTYPE_IMAGE_IN_3D src,
                                DTYPE_IMAGE_OUT_3D dst0,
                                DTYPE_IMAGE_OUT_3D dst1,
                                DTYPE_IMAGE_OUT_3D dst2,
                                DTYPE_IMAGE_OUT_3D dst3,
                                DTYPE_IMAGE_OUT_3D dst4,
                                DTYPE_IMAGE_OUT_3D dst5,
                                DTYPE_IMAGE_OUT_3D dst6,
                                DTYPE_IMAGE_OUT_3D dst7,
                                DTYPE_IMAGE_OUT_3D dst8,
                                DTYPE_IMAGE_OUT_3D dst9,
                                DTYPE_IMAGE_OUT_3D dst10,
                                DTYPE_IMAGE_OUT_3D dst11
                            ){

   const int i = get_global_id(0), j = get_global_id(1), k = get_global_id(2);

   WRITE_IMAGE_3D(dst0,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(const int4)(i,j,12*k,0)).x);
   WRITE_IMAGE_3D(dst1,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+1,0)).x);
   WRITE_IMAGE_3D(dst2,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+2,0)).x);
   WRITE_IMAGE_3D(dst3,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+3,0)).x);
   WRITE_IMAGE_3D(dst4,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+4,0)).x);
   WRITE_IMAGE_3D(dst5,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+5,0)).x);
   WRITE_IMAGE_3D(dst6,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+6,0)).x);
   WRITE_IMAGE_3D(dst7,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+7,0)).x);
   WRITE_IMAGE_3D(dst8,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+8,0)).x);
   WRITE_IMAGE_3D(dst9,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+9,0)).x);
   WRITE_IMAGE_3D(dst10,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+10,0)).x);
   WRITE_IMAGE_3D(dst11,(int4)(i,j,k,0),(DTYPE_OUT)READ_IMAGE_3D(src,sampler,(int4)(i,j,12*k+11,0)).x);
}
