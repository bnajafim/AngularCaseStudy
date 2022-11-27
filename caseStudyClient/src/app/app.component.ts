import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-caseStudy',
  templateUrl: './app.component.html',
  //styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title: string = '';
constructor(private router: Router) {
// if they refresh the page and setTitle doesn't run we'll grab the window path
this.title === ''
? this.setTitle(
window.location.pathname.substring(1, window.location.pathname.length)
)
: null;
}
setTitle(title: string) {
this.title = title;
}
}
