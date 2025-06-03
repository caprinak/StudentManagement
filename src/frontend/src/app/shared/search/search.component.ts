import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
  @Output() searchInputEvent = new EventEmitter<string>();
  onSearchInputChange(searchValue: string): void {
    this.searchInputEvent.emit(searchValue);
  }
}