import 'package:flutter/material.dart';
import 'package:harpy/components/common/animations/explicit/fade_animation.dart';
import 'package:harpy/components/common/animations/explicit/slide_in_animation.dart';

class SecondaryHeadline extends StatelessWidget {
  const SecondaryHeadline(
    this.text, {
    this.delay = Duration.zero,
  });

  final String text;
  final Duration delay;

  @override
  Widget build(BuildContext context) {
    return FadeAnimation(
      curve: Curves.easeInOut,
      duration: const Duration(seconds: 1),
      delay: delay,
      child: SlideInAnimation(
        offset: const Offset(0, 50),
        curve: Curves.easeOut,
        duration: const Duration(seconds: 1),
        delay: delay,
        child: Text(
          text,
          style: Theme.of(context).textTheme.headline4,
        ),
      ),
    );
  }
}

class PrimaryHeadline extends StatelessWidget {
  const PrimaryHeadline(
    this.text, {
    this.delay = Duration.zero,
  });

  final String text;
  final Duration delay;

  @override
  Widget build(BuildContext context) {
    // todo: maybe use Curves.easeOutCubic
    // todo: text overlflow
    return FadeAnimation(
      curve: Curves.easeInOut,
      duration: const Duration(seconds: 2),
      delay: delay,
      child: SlideInAnimation(
        curve: Curves.easeOutQuad,
        duration: const Duration(seconds: 2),
        offset: const Offset(0, 75),
        child: Text(
          text,
          style: Theme.of(context).textTheme.headline2,
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}