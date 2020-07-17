import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Movie } from 'src/app/model/movie';
import { MOVIE_API_URL } from './../../constant/constant';

@Injectable({
  providedIn: 'root'
})
export class MovieDataService {

  constructor(private http: HttpClient) { }

  getallData() {
    return this.http.get(MOVIE_API_URL);
  }

  getOneMovie(id: string) {
    return this.http.get(MOVIE_API_URL + "/" + id);
  }

  addMovie(movie: Movie, userId: string) {
    return this.http.post(MOVIE_API_URL + "?userId=" + userId, movie);
  }

  updateMovie(id: string, movie: Movie, userId: string) {
    return this.http.put(MOVIE_API_URL + "/" + id + "?userId=" + userId, movie);
  }

  deleteMovie(id: string, userId: string) {
    return this.http.delete(MOVIE_API_URL + "/" + id + "?userId=" + userId);
  }
}
