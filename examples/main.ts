import express from "npm:express";
import { parse } from "jsr:@std/yaml@0.224.0"

export function add(a: number, b: number): number {
  express();
  parse("a: 1\nb: 2\n");
  return a + b;
}

// Learn more at https://deno.land/manual/examples/module_metadata#concepts
if (import.meta.main) {
  console.log("Add 2 + 3 =", add(2, 3));
}
