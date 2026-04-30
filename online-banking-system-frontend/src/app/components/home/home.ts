import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faArrowRightToBracket, 
  faUserPlus, 
  faShieldHeart,
  faBolt,
  faMobileScreen,
  faFlag,
  faMoneyBillWave,
  faCreditCard,
  faCoins,
  faShield,
  faLock,
  faHandHoldingDollar,
  faSackDollar,
  faEnvelope,
  faPhone,
  faHeadset,
  faWallet,
  faLandmark,
  faFileLines,
  faScaleBalanced
} from '@fortawesome/free-solid-svg-icons';
import { 
  faFacebook, 
  faTwitter, 
  faInstagram, 
  faLinkedin 
}from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-home',
  imports: [FontAwesomeModule,RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
// Hero Icons
  faArrowRightToBracket = faArrowRightToBracket;
  faUserPlus = faUserPlus;
  
  // Feature Icons
  faShieldHeart = faShieldHeart;
  faBolt = faBolt;
  faMobileScreen = faMobileScreen;
  faFlag = faFlag;
  
  // Account Card Icons
  faMoneyBillWave = faMoneyBillWave;
  faCreditCard = faCreditCard;
  faCoins = faCoins;
  faShield = faShield;
  faLock = faLock;
  faHandHoldingDollar = faHandHoldingDollar;
  faSackDollar = faSackDollar;
  
  // Footer Icons
  faFacebook = faFacebook;
  faTwitter = faTwitter;
  faInstagram = faInstagram;
  faLinkedin = faLinkedin;
  faEnvelope = faEnvelope;
  faPhone = faPhone;
  faHeadset = faHeadset;
  faWallet = faWallet;
  faLandmark = faLandmark;
  faFileLines = faFileLines;
  faScaleBalanced = faScaleBalanced;

}
