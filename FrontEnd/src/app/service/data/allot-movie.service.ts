import { Injectable } from '@angular/core';

import { MOVIE_API_URL } from './../../constant/constant';
import { HttpClient } from '@angular/common/http';
import { MovieMultiplex} from 'src/app/model/movie-multiplex';

@Injectable({
  providedIn: 'root'
})
export class AllotMovieService {
  constructor(private http: HttpClient) { }

  getallData() {
    return this.http.get(MOVIE_API_URL + "/movieMultiplex");
  }

  getOneMovieMultiplex(id: string) {
    return this.http.get(MOVIE_API_URL + "/movieMultiplex/" + id);
  }

  addMovieMultiplex(userId: string, movieMultiplex: MovieMultiplex) {
    return this.http.post(MOVIE_API_URL + "/movieMultiplex" + "?userId=" + userId, movieMultiplex);
  }

  updateMovieMultiplex(id: string, movieMultiplex: MovieMultiplex, userId: string) {
    console.log(userId);
    return this.http.put(MOVIE_API_URL + "/movieMultiplex/" + id + "?userId=" + userId, movieMultiplex);
  }

  deleteMovieMultiplex(id: string, userId: string) {
    return this.http.delete(MOVIE_API_URL + "/movieMultiplex/" + id + "?userId=" + userId);
  }
}
