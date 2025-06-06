import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule , routingComponent} from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';

import { StudentService } from './service/student.service';
import { CohortService } from './service/cohort.service';
import { FacultyService } from './service/faculty.service';
import { CourseService } from './service/course.service';
import { ResultService } from './service/result.service';
import { LibraryCardService } from './service/library-card.service';
import { SearchComponent } from './shared/search/search.component';


@NgModule({
  declarations: [
    AppComponent,
    routingComponent,
    SearchComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule, //httpClient
    FormsModule, //Form
    AppRoutingModule
  ],
  providers: [StudentService,CohortService,FacultyService,CourseService,ResultService,LibraryCardService], //Declare Service
  bootstrap: [AppComponent]
})
export class AppModule { }
