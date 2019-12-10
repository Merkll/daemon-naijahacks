import mongoose from 'mongoose';

const messageSchema = new mongoose.Schema({
  content: {
    type: String,
  },
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
  },
  contact: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Contact',
  },
});

const Message = mongoose.model('Message', messageSchema);

export default Message;
