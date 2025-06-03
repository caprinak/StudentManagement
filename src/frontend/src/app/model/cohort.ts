import { Faculty } from './faculty';
import { Student } from './student';

import { Faculty } from './faculty';
import { Student } from './student';

export interface Cohort {
    id?: number;
    name: string;
    faculty?: Faculty;
    students?: Student[];
}
