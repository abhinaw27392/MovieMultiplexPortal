import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { LoginComponent } from './login/login.component';
import { MovieManagementComponent } from './movie-management/movie-management.component';
import { MultiplexManagementComponent } from './multiplex-management/multiplex-management.component';
import { AllotMovieComponent } from './allot-movie/allot-movie.component';
import { ErrorComponent } from './error/error.component';


const routes: Routes = [
  { path: "", redirectTo: "home", pathMatch: "full" },
  { path: "home", component: HomeComponent },
  { path: "login", component: LoginComponent },
  { path: "movie", component: MovieManagementComponent },
  { path: "multiplex", component: MultiplexManagementComponent },
  { path: "allotedMovie", component: AllotMovieComponent },

  // search route
  { path: "search", component: SearchComponent },
  // fallback  mapping
  { path: "**", component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
