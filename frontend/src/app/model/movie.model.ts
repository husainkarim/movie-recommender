export class Movie {
  id: number;
  title: string;
  genres: Genre[];
  directors: Person[];
  actors: Person[];
  rated: Rate[];
  released: number;
  tagline: string;
  averageRating: number;

  constructor(data: {
    id: number;
    title: string;
    genres: Genre[];
    directors: Person[];
    actors: Person[];
    rated: Rate[];
    released: number;
    tagline: string;
  }) {
    this.id = data.id;
    this.title = data.title;
    this.genres = data.genres;
    this.directors = data.directors;
    this.actors = data.actors;
    this.rated = data.rated;
    this.released = data.released;
    this.tagline = data.tagline;
    if (data.rated.length > 0) {
      this.averageRating = data.rated.reduce((sum, r) => sum + r.rating, 0) / data.rated.length;
    } else {
      this.averageRating = 0;
    }
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
  id: number;
  name: string;
  birthDate: number;
}

export interface Genre {
  id: number;
  name: string;
}

export interface Rate {
  movieId: number;
  userId: number;
  rating: number;
  user: User;
}

export interface User {
  id: number;
  email: string;
  role: string;
}
