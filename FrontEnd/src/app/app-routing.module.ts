import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { LoginComponent } from './login/login.component';
import { MovieManagementComponent } from './movie-management/movie-management.component';
import { MultiplexManagementComponent } from './multiplex-management/multiplex-management.component';
import { AllotMovieComponent } from './allot-movie/allot-movie.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AuthGuardService } from './service/auth-guard.service';


const routes: Routes = [
  { path: "", redirectTo: "home", pathMatch: "full" },
  { path: "home", component: HomeComponent },
  { path: "login", component: LoginComponent },
  { path: "register", component: SignUpComponent },

  // search route
  { path: "search", component: SearchComponent },

  // secure route
  { path: "movie", component: MovieManagementComponent, canActivate: [AuthGuardService] },
  { path: "multiplex", component: MultiplexManagementComponent, canActivate: [AuthGuardService] },
  { path: "allotedMovie", component: AllotMovieComponent, canActivate: [AuthGuardService] },

  // fallback  mapping
  { path: "**", component: HomeComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
