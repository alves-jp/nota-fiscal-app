import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  constructor(private router: Router) {}

  navigateTo(url: string) {
    this.router.navigate([url]);
  }

  goToIDSWebsite() {
    window.open('https://ids.inf.br/', '_blank');
  }
}