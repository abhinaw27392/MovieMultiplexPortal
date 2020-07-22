import {Movie} from './movie';
import {Multiplex} from './multiplex';
import {UserDetail} from './userDetail';

export class MovieMultiplexDetails {

    constructor(public id: string, public movie: Movie, public multiplex: Multiplex,
        public screenName: string, public userDetail: UserDetail) {

    }
}