import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { DataTablesModule } from 'angular-datatables';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { MovieManagementComponent } from './movie-management/movie-management.component';
import { MultiplexManagementComponent } from './multiplex-management/multiplex-management.component';
import { AllotMovieComponent } from './allot-movie/allot-movie.component';
import { SearchComponent } from './search/search.component';
import { ErrorComponent } from './error/error.component';
import { AlertComponent } from './alert/alert.component';



@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    MovieManagementComponent,
    MultiplexManagementComponent,
    AllotMovieComponent,
    SearchComponent,
    ErrorComponent,
    AlertComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    DataTablesModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
