import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MovieSearchCriteria } from '../../model/movie.model';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {
  @Input() genres: string[] = [];
  @Input() years: number[] = [];
  @Output() readonly searchChanged = new EventEmitter<MovieSearchCriteria>();

  criteria: MovieSearchCriteria = {
    title: '',
    genre: '',
    year: 0
  };

  onSearchChanged(): void {
    this.searchChanged.emit({ ...this.criteria });
  }

  clearFilters(): void {
    this.criteria = { title: '', genre: '', year: 0 };
    this.onSearchChanged();
  }

}
