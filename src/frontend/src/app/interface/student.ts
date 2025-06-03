import { Gender } from "../enum/gender.enum";
import { Cohort } from "./cohort";

export interface Student
{
  id: number;
  name: string;
  email: string;
  gender: Gender;
  address: string;
  dob: Date;
  cohort: Cohort;
}
