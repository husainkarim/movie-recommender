export class Movie {
  id: string;
  title: string;
  genres: Genre[];
  directors: Person[];
  actors: Person[];
  ratings: RateMovie[];
  released: number;
  tagline: string;
  averageRating: number;

  constructor(data: {
    id: string;
    title: string;
    genres: Genre[];
    directors: Person[];
    actors: Person[];
    ratings: RateMovie[];
    released: number;
    tagline: string;
    averageRating: number;
  }) {
    this.id = data.id;
    this.title = data.title;
    this.genres = data.genres;
    this.directors = data.directors;
    this.actors = data.actors;
    this.ratings = data.ratings;
    this.released = data.released;
    this.tagline = data.tagline;
    this.averageRating = data.averageRating;
  }
}

export interface MovieSearchCriteria {
  title: string;
  genre: string;
  year: number;
}

export interface RecommendationFilter {
  genre: string;
  year: number;
}

export interface Person {
  id: string;
  name: string;
  birthDate: number;
}

export interface Genre {
  id: string;
  name: string;
}

export interface RateMovie {
  id: string;
  rating: number;
  timestamp: number;
  user: User;
}

export interface RateUser {
  id: string;
  rating: number;
  timestamp: number;
  movie: Movie;
}

export class User {
  id: string;
  email: string;
  role: string;
  ratings: RateUser[];
  watchlist: WatchList[];

  constructor(data: {
    id: string;
    email: string;
    role: string;
    ratings: RateUser[];
    watchlist: WatchList[];
    }) {
    this.id = data.id;
    this.email = data.email;
    this.role = data.role;
    this.ratings = data.ratings;
    this.watchlist = data.watchlist;
    }
}

export interface WatchList {
  id: string;
  movie: Movie;
}
