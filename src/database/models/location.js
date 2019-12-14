import { Schema, model } from 'mongoose';

const locationShema = new Schema({
  home: {
    type: String,
    required: true,
  },
  work: {
    type: String,
  },
  user: {
    type: Schema.Types.ObjectId,
    ref: 'User',
  },
});

const Location = model('Location', locationShema);

export default Location;
