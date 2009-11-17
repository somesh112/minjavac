Building symbol table...
Typechecking...
Building IR...
IR:

class QuickSort:

procedure QuickSort::main
[]
   .new_QS := new QS;
   save_context;
   param .new_QS;
   param 10;
   .call := call QS::Start;
   load_context;
   print .call;
end
end

class QS:

procedure QS::Start
[]
   save_context;
   param this;
   param sz;
   .call := call QS::Init;
   load_context;
   aux01 := .call;
   save_context;
   param this;
   .call_A := call QS::Print;
   load_context;
   aux01 := .call_A;
   print 9999;
   .sub := sub size, 1;
   aux01 := .sub;
   save_context;
   param this;
   param aux01;
   param 0;
   .call_B := call QS::Sort;
   load_context;
   aux01 := .call_B;
   save_context;
   param this;
   .call_C := call QS::Print;
   load_context;
   aux01 := .call_C;
   return 0;
end

procedure QS::Sort
[20, 1]
[2]
[19, 3]
[4]
[9, 5]
[7, 6]
[8]
[8]
[4]
[10]
[15, 11]
[13, 12]
[14]
[14]
[10]
[17, 16]
[18]
[18]
[2]
[21]
[21]
[]
   t := 0;
   if greater_or_equal(left, right) goto .if_false;

   .array_lookup := number[right];
   v := .array_lookup;
   .sub := sub left, 1;
   i := .sub;
   j := right;
   cont01 := 1;

 .loop:
   if is_false cont01 goto .while_false;

   cont02 := 1;

 .loop_A:
   if is_false cont02 goto .while_false_A;

   .add := add i, 1;
   i := .add;
   .array_lookup_A := number[i];
   aux03 := .array_lookup_A;
   if less_than(aux03, v) goto .if_false_A;

   cont02 := 0;
   goto .if_next_A;

 .if_false_A:
   cont02 := 1;

 .if_next_A:
   goto .loop_A;

 .while_false_A:
   cont02 := 1;

 .loop_B:
   if is_false cont02 goto .while_false_B;

   .sub_A := sub j, 1;
   j := .sub_A;
   .array_lookup_B := number[j];
   aux03 := .array_lookup_B;
   if less_than(v, aux03) goto .if_false_B;

   cont02 := 0;
   goto .if_next_B;

 .if_false_B:
   cont02 := 1;

 .if_next_B:
   goto .loop_B;

 .while_false_B:
   .array_lookup_C := number[i];
   t := .array_lookup_C;
   .array_lookup_D := number[j];
   number[i] := .array_lookup_D;
   number[j] := t;
   .add_A := add i, 1;
   if greater_or_equal(j, .add_A) goto .if_false_C;

   cont01 := 0;
   goto .if_next_C;

 .if_false_C:
   cont01 := 1;

 .if_next_C:
   goto .loop;

 .while_false:
   .array_lookup_E := number[i];
   number[j] := .array_lookup_E;
   .array_lookup_F := number[right];
   number[i] := .array_lookup_F;
   number[right] := t;
   save_context;
   param this;
   .sub_B := sub i, 1;
   param .sub_B;
   param left;
   .call := call QS::Sort;
   load_context;
   nt := .call;
   save_context;
   param this;
   param right;
   .add_B := add i, 1;
   param .add_B;
   .call_A := call QS::Sort;
   load_context;
   nt := .call_A;
   goto .if_next;

 .if_false:
   nt := 0;

 .if_next:
   return 0;
end

procedure QS::Print
[1]
[3, 2]
[1]
[]
   j := 0;

 .loop:
   if greater_or_equal(j, size) goto .while_false;

   .array_lookup := number[j];
   print .array_lookup;
   .add := add j, 1;
   j := .add;
   goto .loop;

 .while_false:
   return 0;
end

procedure QS::Init
[]
   size := sz;
   .new_array := new[] sz;
   number := .new_array;
   number[0] := 20;
   number[1] := 7;
   number[2] := 12;
   number[3] := 18;
   number[4] := 2;
   number[5] := 11;
   number[6] := 6;
   number[7] := 9;
   number[8] := 19;
   number[9] := 5;
   return 0;
end
end

Ok!