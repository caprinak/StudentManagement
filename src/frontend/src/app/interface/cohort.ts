import { Faculty } from "./faculty";

export interface Cohort
{
  id: number;
  name: string;
  faculty: Faculty;
}
