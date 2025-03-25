import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  template: `
    <div class="home-container">
      <h1>Bem-vindo à Home</h1>
      <p>Esta é a página inicial da aplicação.</p>
    </div>
  `,
  styles: [
    `
    .home-container {
      text-align: center;
      margin-top: 50px;
    }
    h1 {
      color: green;
    }
    p {
      font-size: 18px;
    }
    `
  ]
})
export class HomeComponent { }