import models from '../models';
import { uid1, uid2 } from './user';

const location1 = {
  home: '1 Jagunmolu street, Bariga',
  work: '20 Elenu street, Bariga',
  user: uid1,
};

const location2 = {
  home: '2 Aminu Street, Maryland',
  work: '2 Montgomery road, Yaba',
  user: uid2,
};


const seedLocation = () => models.Location.insertMany([location1, location2]);

export default seedLocation;
