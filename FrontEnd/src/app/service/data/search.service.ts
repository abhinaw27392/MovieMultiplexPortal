import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MOVIE_API_URL, MULTIPLEX_API_URL } from 'src/app/constant/constant';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  constructor(private http: HttpClient) { }

  searchMovies(searchString: string) {
    return this.http.get(MOVIE_API_URL + "/searchMovie/" + searchString);
  }

  searchMultiplexes(searchString: string) {
    return this.http.get(MULTIPLEX_API_URL + "/searchMultiplex/" + searchString);
  }
}
